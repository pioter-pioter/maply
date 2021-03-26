import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { PositionService, StreamData } from 'src/app/service/position.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit, OnDestroy {

  users: StreamData[] = [];
  private eventSource$: Observable<StreamData>;
  private eventSourceSubscription: Subscription;

  constructor(private positionService: PositionService) { }

  ngOnInit(): void {
    this.eventSource$ = this.positionService.getDataFromEventSource('stream-1');
    this.getDataFromEventSource();
  } 

  ngOnDestroy(): void {
    this.eventSourceSubscription.unsubscribe();
  }

  getDataFromEventSource() {
    this.eventSourceSubscription = this.eventSource$.subscribe(
      (streamData: StreamData) => {
        let username = streamData.value.username;
      let element = this.users.find((item: StreamData) => item.value.username === username)
      if (element) {
        element.id.sequence = streamData.id.sequence;
        element.id.timestamp = streamData.id.timestamp;
        element.id.value = streamData.id.value;
        element.value.lat = streamData.value.lat;
        element.value.lng = streamData.value.lng;
      }
      else {
        this.users.push(streamData);
      }
    });
  }
  
}
