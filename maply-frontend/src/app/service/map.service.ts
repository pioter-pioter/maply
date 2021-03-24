import { Injectable } from '@angular/core';
import * as e from '../../environments/environment';
import { Map } from 'mapbox-gl';

@Injectable({
  providedIn: 'root'
})
export class MapService {
  constructor() { }
  getMap(): Map {
    const lat: number = 50.049683;
    const lng: number = 19.944544;
    const zoom: number = 12;
    const m: Map = new Map({
      accessToken: e.environment.mapboxAccessToken,
      container: 'my-map',
      style: 'mapbox://styles/mapbox/streets-v11',
      center: [lng, lat],
      zoom: zoom
    });
    return m;
  }
}

