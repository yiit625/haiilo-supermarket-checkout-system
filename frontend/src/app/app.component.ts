import { Component } from '@angular/core';
import {MatToolbarModule} from '@angular/material/toolbar';
import {ProductListComponent} from './components/product-list/product-list.component';
import {CheckoutPanelComponent} from './components/checkout-panel/checkout-panel.component';

@Component({
  selector: 'app-root',
  imports: [
    MatToolbarModule,
    ProductListComponent,
    CheckoutPanelComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'supermarket-ui';
}
