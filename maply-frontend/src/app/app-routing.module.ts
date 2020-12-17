import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MapComponent } from './component/map/map.component';
import { UserDetailsComponent } from './component/user-details/user-details.component';


const routes: Routes = [
  {
    path: "positions",
    component: MapComponent
  },
  
  {
    path: "user/:username",
    component: UserDetailsComponent
  },
  {
    path: "",
    redirectTo: "/positions",
    pathMatch: "full" 
  },
  {
    path: "**",
    redirectTo: "/positions",
    pathMatch: "full" 
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
