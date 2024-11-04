import {Component, OnInit} from '@angular/core';
import {Universite} from "../../models/Universite";
import {UniversiteService} from "../../services/UniversiteService";



@Component({
  selector: 'app-universite',
  templateUrl: './universite.component.html',
  styleUrls: ['./universite.component.css']
})
export class UniversiteComponent implements OnInit{
  universites: Universite[] = [];
  selectedUniversite: Universite = { nomUniv: '' }; // Initialize with an object
  isAddModalOpen = false;
  isUpdateModalOpen = false;

  constructor(private universiteService: UniversiteService) {}

  ngOnInit(): void {
    this.loadUniversites();
  }

  loadUniversites(): void {
    this.universiteService.getAllUniversites().subscribe(
      (data) => {
        this.universites = data;
      },
      (error) => {
        console.error('Error loading universities', error);
      }
    );
  }

  openAddModal() {
    this.selectedUniversite = { nomUniv: '' }; // Reset selected university for adding
    this.isAddModalOpen = true; // Open the add modal
  }

  openUpdateModal(universite: Universite): void {
    this.selectedUniversite = { ...universite }; // Copy the selected university data
    this.isUpdateModalOpen = true; // Open the update modal
  }

  closeAddModal() {
    this.isAddModalOpen = false; // Close the add modal
  }

  closeUpdateModal() {
    this.isUpdateModalOpen = false; // Close the update modal
  }

  addUniversite(): void {
    if (this.selectedUniversite && this.selectedUniversite.nomUniv && this.selectedUniversite.nomUniv.trim()) {
      this.universiteService.addUniversite(this.selectedUniversite).subscribe((newUniversite: Universite) => {
        this.universites.push(newUniversite); // Add the new university to the list
        this.closeAddModal(); // Close the modal after adding
      });
    }
  }

  updateUniversite(): void {
    // Similar check for update
    if (this.selectedUniversite && this.selectedUniversite.nomUniv) {
      this.universiteService.updateUniversite(this.selectedUniversite).subscribe(() => {
        // Update the local list of universities
        const index = this.universites.findIndex(u => u.idUniv === this.selectedUniversite.idUniv);
        if (index !== -1) {
          this.universites[index] = this.selectedUniversite; // Update the existing university
        }
        this.closeUpdateModal(); // Close the modal after updating
      });
    }
  }

  deleteUniversite(id: number): void {
    if (confirm('Are you sure you want to delete this university?')) {
      this.universiteService.deleteUniversite(id).subscribe(() => {
        this.universites = this.universites.filter(univ => univ.idUniv !== id); // Remove deleted university
      });
    }
  }
}
