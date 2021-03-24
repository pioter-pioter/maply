import { Feature, Point } from 'geojson';
import { StreamData } from '../service/position.service'

export class PointFeature implements Feature<Point, StreamData> {
    type: 'Feature';
    geometry: Point;
    id?: string | number;
    properties: StreamData;
    constructor(streamData: StreamData) {
      this.type = 'Feature';
      this.id = streamData.id.value;
      this.properties = streamData;
      this.geometry = {
        type: "Point",
        coordinates: [this.properties.value.lng, this.properties.value.lat]
      }
    }
}

