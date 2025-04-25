import { ChangeDetectorRef, Component, PLATFORM_ID, inject } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { CardModule } from 'primeng/card';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { DatePickerModule } from 'primeng/datepicker';
import { FormsModule } from '@angular/forms';
import { PostComponent } from '../posts/post/post.component';
import { PostControllerService, PostDTO } from '../api';

@Component({
  selector: 'app-dashboard',
  imports: [ChartModule,CardModule,DatePickerModule,FormsModule,PostComponent,CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
    chartData: any;
    sentimentPieData : any;
    sentimentPieOptions : any;
    negativePosts : PostDTO[] = [];
    positivePosts : PostDTO[] = [];
    neutralPosts : PostDTO[] = [];
    rangeDates :Date[] | null | undefined = undefined;
    topPost : PostDTO|undefined;
     platformId = inject(PLATFORM_ID);
    constructor(private postController : PostControllerService,private cd : ChangeDetectorRef){}
    ngOnInit() {
        this.loadChartData();
        this.initChart();
    }
    loadChartData() {
        this.postController.getPostCountsOverTime().subscribe(data => {
            this.chartData = {
                labels: data.map(d => d.month),
                    datasets: [
                    {
                        label: 'Posts per Month',
                        data: data.map(d => d.count),
                            backgroundColor: '#42A5F5'
                    }
                ]
            };
        });
    }
     initChart() {
        this.postController.getPostivePosts().subscribe(data =>{
            this.positivePosts=data;

            this.postController.getNeutralPosts().subscribe(data =>{
                this.neutralPosts=data;

                this.postController.getNegativePosts().subscribe(data =>{
                    this.negativePosts=data;

                    if (isPlatformBrowser(this.platformId)) {
                        const documentStyle = getComputedStyle(document.documentElement);
                        const textColor = documentStyle.getPropertyValue('--text-color');

                        this.sentimentPieData = {
                            labels: ['Positive', 'Neutral', 'Negative'],
                            datasets: [
                                {
                                    data: [this.positivePosts.length, this.neutralPosts.length, this.negativePosts.length],
                                    backgroundColor: [documentStyle.getPropertyValue('--p-cyan-500'), documentStyle.getPropertyValue('--p-orange-500'), documentStyle.getPropertyValue('--p-gray-500')],
                                    hoverBackgroundColor: [documentStyle.getPropertyValue('--p-cyan-400'), documentStyle.getPropertyValue('--p-orange-400'), documentStyle.getPropertyValue('--p-gray-400')]
                                }
                            ]
                        };

                        this.sentimentPieOptions = {
                            plugins: {
                                legend: {
                                    labels: {
                                        usePointStyle: true,
                                        color: textColor
                                    }
                                }
                            }
                        };
                        this.cd.markForCheck()
                    }
                });
            });
        });
     }
     onDateInput(){
        if(this.rangeDates![1]==undefined)
            return;
        this.postController.getTopPost(this.rangeDates![0].toISOString(),this.rangeDates![1].toISOString()).subscribe(data =>{
            this.topPost=data;
        });
     }
}
