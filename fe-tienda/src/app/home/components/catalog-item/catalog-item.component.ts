import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable, map } from 'rxjs';
import { CatalogItem } from 'src/app/core/models/catalog-item';
import { CartService } from 'src/app/core/services/cart.service';

@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  styleUrls: ['./catalog-item.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CatalogItemComponent implements OnInit {

  @Input() catalogItem!: CatalogItem;
  @Output() catalogItemAdd = new EventEmitter<void>();
  @Input() id!: number;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    console.log("CatalogItemComponent created");
  }

  addToCart() {
    this.catalogItemAdd.emit();
  }

  buy() {
    alert("TODO");
  }

  get cartItemsQuantity$() : Observable<number> {
    return this.cartService?.countCartItemsByCatalogoId(this.id);
  }

  get isEmptyByCartItem$() {
    return this.cartItemsQuantity$.pipe(
      map(qty => qty <= 0)
    );
  }
}