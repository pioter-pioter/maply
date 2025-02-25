import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import * as e from '../../environments/environment';
import { RequestData } from '../class/request-data';

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  constructor(private http: HttpClient) { }

  getDataFromEventSource(streamId: string): Observable<StreamData> {
    return new Observable<StreamData>((subscriber) => {
      let es: EventSource;
      es = new EventSource(`${e.environment.gatewayPositionSse}${streamId}`);
      es.onmessage = (messageEvent: MessageEvent) => {
        let sd: StreamData = JSON.parse(messageEvent.data);
        subscriber.next(sd);
      }
      es.onerror = (error: Event) => {
        es.close();
        subscriber.error(error);
      }
    })
  }

  getWebSocket(streamId: string): WebSocketSubject<StreamData | RequestData> {
    return webSocket({
      url: `${e.environment.gatewayPositionWs}${streamId}`
    })
  }

  getDataFromStream(streamId: string, 
    from?: number, 
    to?: number, 
    username?: string): Observable<StreamData[]> {
    let p: HttpParams = new HttpParams();
    from && (p = p.append('from', `${from}-0`));
    to && (p = p.append('to', `${to}-0`));
    username && (p = p.append('username', `${username}`));
    return this.http.get<StreamData[]>(`${e.environment.gatewayPositionApi}${streamId}`, {
      params: p
    });
  }
  
}

export interface StreamData {
  stream: string,
  value: {
    username: string,
    lat: number,
    lng: number
  }
  id: {
    value: string,
    timestamp: string,
    sequence: string
  }
}