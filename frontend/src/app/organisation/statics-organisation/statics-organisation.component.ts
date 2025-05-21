import { Component, OnInit } from '@angular/core';
import { OrganisationControllerService } from '../../../api/services/services/organisation-controller.service';
import { OrganizationEntityCountsDto } from '../../../api/services/models/organization-entity-counts-dto';
import { ChartModule } from 'primeng/chart';

@Component({
    selector: 'app-statics-organisation',
    standalone: true,
    imports: [ChartModule],
    templateUrl: './statics-organisation.component.html',
    styleUrls: ['./statics-organisation.component.scss']
})
export class StaticsOrganisationComponent implements OnInit {
    // Données pour les graphiques
    officeData: any;
    feedbackData: any;
    userData: any;

    // Options de graphiques
    barOptions: any;
    pieOptions: any;
    lineOptions: any;

    constructor(private organisationService: OrganisationControllerService) {}

    ngOnInit(): void {
        this.loadOrganizationStats();
    }

    loadOrganizationStats(): void {
        this.organisationService.getAllOrganizationsEntityCounts().subscribe((data: OrganizationEntityCountsDto[]) => {
            // Données pour le graphique en barre (Offices par Organisation)
            this.officeData = {
                labels: data.map(org => org.organizationName),
                datasets: [
                    {
                        label: 'Offices Count',
                        data: data.map(org => org.officeCount),
                        backgroundColor: '#42A5F5',
                        borderColor: '#42A5F5',
                        borderWidth: 1
                    }
                ]
            };

            // Données pour le graphique en secteur (Feedbacks par Organisation)
            this.feedbackData = {
                labels: data.map(org => org.organizationName),
                datasets: [
                    {
                        data: data.map(org => org.feedbackCount),
                        backgroundColor: ['#66BB6A', '#FFA726', '#FF7043'],
                        hoverBackgroundColor: ['#66BB6A', '#FFA726', '#FF7043']
                    }
                ]
            };

            // Données pour le graphique en ligne (Users par Organisation)
            this.userData = {
                labels: data.map(org => org.organizationName),
                datasets: [
                    {
                        label: 'Users Count',
                        data: data.map(org => org.userCount),
                        fill: false,
                        borderColor: '#66BB6A',
                        tension: 0.4
                    }
                ]
            };

            // Options des graphiques
            this.barOptions = {
                responsive: false,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    }
                },
                scales: {
                    x: {
                        beginAtZero: true
                    },
                    y: {
                        beginAtZero: true
                    }
                }
            };


            this.lineOptions = {
                responsive: false,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    }
                },
                scales: {
                    x: {
                        beginAtZero: true
                    },
                    y: {
                        beginAtZero: true
                    }
                }
            };
        });

        this.pieOptions = {
            responsive: false,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top'
                },
                tooltip: {
                    callbacks: {
                        label: (tooltipItem: any) => {
                            return tooltipItem.label + ': ' + tooltipItem.raw;
                        }
                    }
                }
            }
        };

    }
}
