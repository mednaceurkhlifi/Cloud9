import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import * as L from 'leaflet';
import { StyleClassModule } from 'primeng/styleclass';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { GoogleGenAI , Type } from "@google/genai";

interface Location {
    lat: number;
    lng: number;
    title: string;
    distance?: number;
}

@Component({
    selector: 'app-map',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        MatButtonModule,
        MatIconModule,
        StyleClassModule,
        ButtonModule,
        RippleModule
    ],
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit, AfterViewInit {
    private map: L.Map | undefined;
    private markers: L.Marker[] = [];
    private currentLocation: Location | null = null;
    private currentLocationMarker: L.Marker | null = null;
    private routes: L.Polyline[] = [];

    private locations: Location[] = [
        { lat: 36.860943, lng: 10.153157, title: 'Ariana' },
        { lat: 36.760943, lng: 10.253157, title: 'Ben Arous' },
        { lat: 36.660943, lng: 10.353157, title: 'Ben Arous' }
    ];

    constructor(public router: Router) { }

    ngOnInit(): void {
        this.getCurrentLocation();
    }

    ngAfterViewInit(): void {
        this.initMap();
    }

    private getCurrentLocation(): void {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    this.currentLocation = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                        title: 'Your Location'
                    };
                    this.updateMapWithCurrentLocation();
                },
                (error) => {
                    console.error('Error getting location:', error);
                }
            );
        }
    }

    private updateMapWithCurrentLocation(): void {
        if (this.map && this.currentLocation) {
            // Create a custom icon for current location
            const currentIcon = L.icon({
                iconUrl: 'assets/layout/images/current-location.svg',
                iconSize: [32, 32],
                iconAnchor: [16, 16],
                popupAnchor: [0, -16]
            });

            // Add current location marker
            this.currentLocationMarker = L.marker(
                [this.currentLocation.lat, this.currentLocation.lng],
                { icon: currentIcon }
            )
                .bindPopup('Your Location')
                .addTo(this.map);

            // Add a pulsing circle around the current location
            L.circle([this.currentLocation.lat, this.currentLocation.lng], {
                radius: 100,
                color: '#2196F3',
                fillColor: '#2196F3',
                fillOpacity: 0.2,
                className: 'pulse-circle'
            }).addTo(this.map);

            // Calculate distances and update markers
            this.calculateDistances();
        }
    }

    public calculateDistances(): void {
        if (!this.currentLocation) return;

        // Calculate distance to each location
        this.locations.forEach(location => {
            location.distance = this.calculateDistance(
                this.currentLocation!.lat,
                this.currentLocation!.lng,
                location.lat,
                location.lng
            );
        });

        // Sort locations by distance
        this.locations.sort((a, b) => (a.distance || 0) - (b.distance || 0));

        // Update markers with new colors
        this.updateMarkers();
    }

    private calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
        const R = 6371; // Radius of the earth in km
        const dLat = this.deg2rad(lat2 - lat1);
        const dLon = this.deg2rad(lon2 - lon1);
        const a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }

    aiResponseText: string | undefined = '';
    async calculateDistancesWithAI(): Promise<void> {
        if (!this.currentLocation) {
            console.error('Current location is not available.');
            return;
        }

        const ai = new GoogleGenAI({ apiKey: "${{ secrets.GEMINI_SECOND }}" });

        const response = await ai.models.generateContent({
            model: "gemini-1.5-flash",
            contents: "Given the current location: \
                        36°53'55.7\"N 10°11'20.8\"E and the following locations: \
                        lat: 36.660943, lng: 10.353157 : Ariana, \
                        lat: 36.860943, lng: 10.153157 : Ben Arous, \
                        calculate the distances and return the coordinates of the closest one.\
                        just return the location like this {x,y}: location name \
                        DONT SHOW ANYTHING ELSE, JUST THE LOCATION PLEASE.",
            // config: {
            //     responseMimeType: 'application/json',
            //     responseSchema: {
            //         type: Type.ARRAY,
            //         items: {
            //             type: Type.OBJECT,
            //             properties: {
            //                 'LocationCoordinates': {
            //                     type: Type.STRING,
            //                     description: 'location coordinates',
            //                     example: '{x,y}',
            //                     nullable: false,
            //                 },
            //             },
            //             required: ['LocationCoordinates'],
            //         },
            //     },
            // } 
        });

         // Parse the response if it's JSON
        //this.aiResponseText = JSON.stringify(response.text, null, 2); // Format for display
        
          
        this.aiResponseText = response.text;
        console.log(response.text);

    }

    private deg2rad(deg: number): number {
        return deg * (Math.PI / 180);
    }

    private initMap(): void {
        // Create the map
        this.map = L.map('map').setView([36.860943, 10.153157], 10);

        // Add the tile layer (OpenStreetMap)
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(this.map);

        // Add markers for each location
        this.updateMarkers();
    }

    private updateMarkers(): void {
        if (!this.map) return;

        // Clear existing markers and routes
        this.clearMarkers();
        this.clearRoutes();

        // Add markers with appropriate colors
        this.locations.forEach((location, index) => {
            const isClosest = index === 0;
            const markerColor = isClosest ? 'red' : 'blue';
            
            const marker = L.marker([location.lat, location.lng])
                .bindPopup(`${location.title}<br>Distance: ${location.distance?.toFixed(2)} km`)
                .addTo(this.map!);

            // Add a colored circle under the marker
            L.circle([location.lat, location.lng], {
                radius: 100,
                color: markerColor,
                fillColor: markerColor,
                fillOpacity: 0.3
            }).addTo(this.map!);

            this.markers.push(marker);

            // Add route from current location to this marker
            if (this.currentLocation) {
                const route = L.polyline(
                    [
                        [this.currentLocation.lat, this.currentLocation.lng],
                        [location.lat, location.lng]
                    ],
                    {
                        color: markerColor,
                        weight: 3,
                        opacity: 0.7,
                        dashArray: '5, 10'
                    }
                ).addTo(this.map!);

                this.routes.push(route);
            }
        });

        // Fit bounds to show all markers and current location
        if (this.currentLocationMarker) {
            const group = L.featureGroup([...this.markers, this.currentLocationMarker, ...this.routes]);
            this.map.fitBounds(group.getBounds());
        }
    }

    private clearRoutes(): void {
        this.routes.forEach(route => route.remove());
        this.routes = [];
    }

    // Method to add a new marker
    addMarker(lat: number, lng: number, title: string): void {
        if (this.map) {
            const marker = L.marker([lat, lng])
                .bindPopup(title)
                .addTo(this.map);
            this.markers.push(marker);
        }
    }

    // Method to clear all markers
    clearMarkers(): void {
        this.markers.forEach(marker => marker.remove());
        this.markers = [];
    }

    // Method to fit map bounds to show all markers
    fitBounds(): void {
        if (this.map && this.markers.length > 0) {
            const group = L.featureGroup([...this.markers, ...(this.currentLocationMarker ? [this.currentLocationMarker] : []), ...this.routes]);
            this.map.fitBounds(group.getBounds());
        }
    }
} 