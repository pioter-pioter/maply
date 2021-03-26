import { Component, OnDestroy, OnInit } from '@angular/core';
import * as M from 'mapbox-gl';
import { PositionService, StreamData } from 'src/app/service/position.service';
import { WebSocketSubject } from 'rxjs/webSocket';
import { RequestData } from '../../class/request-data';
import { MapService } from 'src/app/service/map.service';
import { map } from 'rxjs/operators';
import { PointFeature } from 'src/app/class/point-feature';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html'
})
export class MapComponent implements OnInit, OnDestroy {

  private map: M.Map;
  private webSocketSubject$: WebSocketSubject<StreamData | RequestData>;
  constructor(private positionService: PositionService,
              private mapService: MapService) { }

  ngOnInit(): void {
    this.loadMap();
    this.webSocketSubject$ = this.positionService.getWebSocket('stream-1');
    this.getDataFromWebSocket();
  }

  ngOnDestroy(): void {
    this.webSocketSubject$.unsubscribe();
  }

  loadMap() {
    this.map = this.mapService.getMap();
    this.map.on('load', () => {
      this.map.addControl(new M.NavigationControl());
      this.map.loadImage('https://docs.mapbox.com/mapbox-gl-js/assets/custom_marker.png',
        (error, image) => {
          if (error) throw error;
          this.map.addImage('marker', image);
          this.map.on('click', (e: M.MapMouseEvent) => {
            this.sendDataToWebSocket(new RequestData('Piotr', e.lngLat));
          });
        });
    })
  }

  sendDataToWebSocket(req: RequestData) {
    this.webSocketSubject$.next(req);
  }
  
  getDataFromWebSocket() {
    this.webSocketSubject$
      .pipe(
        map((streamData: StreamData) => {
          return new PointFeature(streamData);
        })
      ).subscribe((pointFeature: PointFeature) => {
        let username:string = pointFeature.properties.value.username;
        if (this.map.getSource(username)) {
          let layer: M.GeoJSONSource = this.map.getSource(username) as M.GeoJSONSource;
          layer.setData(pointFeature);
        }
        else {
          this.map.addSource(username, {
            type: 'geojson',
            data: pointFeature
          });
          this.map.addLayer({
            id: username,
            source: username,
            type: 'symbol',
            layout: {
              "icon-image": 'marker', 
              'text-field': username,
              'text-font': [
                'Open Sans Semibold',
                'Arial Unicode MS Bold'
              ],
              'text-offset': [0, 1.25],
              'text-anchor': 'top'
            }
          });
        }
      });
  }
  
}
