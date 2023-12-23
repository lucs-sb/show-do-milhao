import { Component, Input } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Subscription } from 'rxjs';
import { Alert, AlertType } from 'src/app/entities/alert';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent {
  @Input() id = 'default-alert';
  @Input() fade = true;

  alerts: Alert[] = [];
  alertSubscription!: Subscription;
  routeSubscription!: Subscription;

  constructor(private router: Router, private alertService: AlertService) { }

  ngOnInit() {
      this.alertSubscription = this.alertService.onAlert(this.id)
          .subscribe(alert => {
              if (!alert.message) {
                  this.alerts = this.alerts.filter(x => x.keepAfterRouteChange);

                  this.alerts.forEach(x => delete x.keepAfterRouteChange);
                  return;
              }

              this.alerts.push(alert);

              if (alert.autoClose) {
                  setTimeout(() => this.removeAlert(alert), 3000);
              }
         });

      this.routeSubscription = this.router.events.subscribe(event => {
          if (event instanceof NavigationStart) {
              this.alertService.clear(this.id);
          }
      });
  }

  ngOnDestroy() {
      this.alertSubscription.unsubscribe();
      this.routeSubscription.unsubscribe();
  }

  removeAlert(alert: Alert) {
      if (!this.alerts.includes(alert)) return;

      const timeout = this.fade ? 250 : 0;
      alert.fade = this.fade;

      setTimeout(() => {
          this.alerts = this.alerts.filter(x => x !== alert);
      }, timeout);
  }

  cssClass(alert: Alert) {
      if (!alert) return;

      const classes = ['alert', 'alert-dismissible'];
              
      const alertTypeClass = {
          [AlertType.Success]: 'alert-success',
          [AlertType.Error]: 'alert-danger',
          [AlertType.Info]: 'alert-info',
          [AlertType.Warning]: 'alert-warning'
      }

      if (alert.type !== undefined) {
          classes.push(alertTypeClass[alert.type]);
      }

      if (alert.fade) {
          classes.push('fade');
      }

      return classes.join(' ');
  }
}
