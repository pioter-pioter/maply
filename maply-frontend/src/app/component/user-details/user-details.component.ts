import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { PositionService, StreamData } from 'src/app/service/position.service';
import { UserData, UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {
  userData: UserData;
  positionHistory: StreamData[] = [];
  constructor(private userService: UserService,
              private positionService: PositionService,
              private route: ActivatedRoute) { }
  ngOnInit(): void {
    this.route.paramMap.subscribe(
      paramMap => {
        this.getData(paramMap.get('username'));
      });
  }
  getData(username: string) {
    forkJoin({
      user: this.userService.getUserByUsername(username),
      position: this.positionService.getDataFromStream('stream-1', undefined, undefined, username)
    })
    .subscribe(
      next => {
        this.userData = next.user;
        this.positionHistory = next.position;
        console.log(JSON.stringify(next.user));
      }
    )
  }
}

