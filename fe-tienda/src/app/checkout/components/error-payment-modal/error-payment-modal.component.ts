import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface ErrorMessage {
  message: string;
}

@Component({
  selector: 'app-error-payment-modal',
  templateUrl: './error-payment-modal.component.html',
  styleUrls: ['./error-payment-modal.component.css']
})
export class ErrorPaymentModalComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: ErrorMessage) { }

  ngOnInit(): void {
    console.log("ErrorPaymentModalComponent created");
  }

}
