import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { CatalogService } from 'src/app/core/services/catalog.service';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  searchForm: FormGroup;

  constructor(private fb: FormBuilder,
    private catalogService: CatalogService) {
  }

  ngOnInit(): void {
    console.log("SearchBarComponent created");
    this.initializeForm();
  }

  initializeForm() {
    this.searchForm = this.fb.group({
      searchTerm: '',
    });
    
     // Escuchar cambios en el campo de búsqueda
    this.searchForm.get('searchTerm').valueChanges.subscribe((term: string) => {
      this.updateSearchTerm(term);
    });
  }

  // Método para filtrar elementos
  updateSearchTerm(term: string): void {
    this.catalogService.updateSearchTerm(term);
  }

}
