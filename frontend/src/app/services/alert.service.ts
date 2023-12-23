import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { filter } from 'rxjs/operators';
import { Alert, AlertOptions, AlertType } from '../entities/alert';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor() { }

  private subject = new Subject<Alert>();
  private defaultId = 'default-alert';

  onAlert(id = this.defaultId): Observable<Alert> {
      return this.subject.asObservable().pipe(filter(x => x && x.id === id));
  }

  success(message: string, options?: AlertOptions) {
      this.alert(new Alert({ ...options, type: AlertType.Success, autoClose: true, message }));
  }

  error(message: string, options?: AlertOptions) {
      this.alert(new Alert({ ...options, type: AlertType.Error, autoClose: true, message }));
  }

  info(message: string, options?: AlertOptions) {
      this.alert(new Alert({ ...options, type: AlertType.Info, autoClose: true, message }));
  }

  warn(message: string, options?: AlertOptions) {
      this.alert(new Alert({ ...options, type: AlertType.Warning, autoClose: true, message }));
  }

  alert(alert: Alert) {
      alert.id = alert.id || this.defaultId;
      this.subject.next(alert);
  }

  clear(id = this.defaultId) {
      this.subject.next(new Alert({ id }));
  }
}
