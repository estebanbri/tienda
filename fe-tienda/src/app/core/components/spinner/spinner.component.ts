import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SpinnerService } from 'src/app/core/services/spinner.service';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent implements OnInit {

  isLoading$: Observable<boolean> = this.spinnerService.isLoading$;

  constructor(private spinnerService:  SpinnerService) { }

  ngOnInit(): void {
    console.log("SpinnerComponent created");
  }

}

