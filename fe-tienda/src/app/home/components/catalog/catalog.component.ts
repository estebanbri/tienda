import { Component, OnInit, OnDestroy } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Observable, debounceTime, distinctUntilChanged, switchMap, filter, map, pipe, of } from 'rxjs';
import { CartItem } from 'src/app/core/models/cart-item';
import { CatalogItem } from 'src/app/core/models/catalog-item';
import { CartService } from 'src/app/core/services/cart.service';
import { CatalogService } from 'src/app/core/services/catalog.service';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit, OnDestroy {

  catalog$: Observable<CatalogItem[]>;
  
  constructor(private catalogService: CatalogService,
    private cartService: CartService,
    private titleService: Title) {
    this.titleService.setTitle('Home | Tienda');
  }

  ngOnInit(): void {
    this.catalogService.loadCatalog();
    this.catalogService.filterSearchTerm$.pipe(
      debounceTime(200), // Espera 200ms después del último evento
      distinctUntilChanged(),
      switchMap( searchTerm => this.catalogService.catalog$.pipe(
      filter(catalogList => catalogList != null ),
      map(catalogList => catalogList.filter(catalogItem => 
        catalogItem.name.toLowerCase().includes(searchTerm.toLowerCase()) 
        || catalogItem.vendor.toLowerCase().includes(searchTerm.toLowerCase()) 
        )))),
      pipe( filteredCatalog => this.catalog$ = filteredCatalog));
  }

  onAddItemToCart(catalogItem: CatalogItem) {
    const cartItem = this.mapCatalogItemToCartItem(catalogItem);
    this.cartService.addItem(cartItem);
  }

  mapCatalogItemToCartItem(catalogItem: CatalogItem): CartItem {
    const { id, name, price, imageUrl } = catalogItem;
    return { catalogoItemId: id, name, price, initialPrice: price, imageUrl, quantity: 1 };
  }

  ngOnDestroy(): void {
    console.log('ngOnDestroy()');
    this.catalogService.updateSearchTerm('');
  }
   
}

