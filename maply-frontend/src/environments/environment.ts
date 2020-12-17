// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,

  // access token
  mapboxAccessToken: 'pk.eyJ1IjoiZGlja2hlYWQxIiwiYSI6ImNraTdqMmdhdDA3OTMyem80azV6cWRzeGoifQ.-ZA4EFkE5a6PJxNkMSaqhQ',

  // position service endpoints
  positionApi: 'http://localhost:8081/api/position/',
  webSocket: 'ws://localhost:8081/ws/position/',
  eventSource: 'http://localhost:8081/sse/position/',

  // user service endpoints
  userApi: 'http://localhost:8082/api/users/'

};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
