import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import * as e from '../../environments/environment'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUsers(): Observable<UserData[]> {
    return this.http.get<UserData[]>(`${e.environment.userApi}`);
  }

  getUserByUsername(username: string): Observable<UserData> {
    return this.http.get<UserData>(`${e.environment.userApi}${username}`);
  }

}

export interface UserData {

  id: number,
  username: string,
  firstName: string,
  lastName: string,
  email: string
  
}
