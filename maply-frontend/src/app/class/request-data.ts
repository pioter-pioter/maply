import { LngLat } from 'mapbox-gl';

export class RequestData {

    username: string;
    lat: number;
    lng: number;

    constructor(username: string, coordinates: LngLat) {
      this.username = username;
      this.lat = coordinates.lat;
      this.lng = coordinates.lng;
    }
    
}

