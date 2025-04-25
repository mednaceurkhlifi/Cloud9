import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { NewsService } from '../../services/news.service';
import { Organisation } from '../../models/Organisation';
import { MonthlyStatsDTO } from '../../models/MonthlyStatsDTO';

@Component({
  selector: 'app-all-stats',
  imports: [CardModule,CommonModule,ChartModule],
  templateUrl: './all-stats.component.html',
  styleUrl: './all-stats.component.scss'
})
export class AllStatsComponent {
  data: any;
  options: any;
  org:Organisation={organizationId:1,name:'myorg'}
  chartData:any;
  constructor(private newsService:NewsService){}
  ngOnInit() {
    this.initChart();
}

initChart() {
  this.newsService.getStats(this.org.organizationId).subscribe((res)=>{
    let stats=res as MonthlyStatsDTO[]
    this.chartData={
      labels:stats.map(s=>s.month),
      datasets:[

        {
          label:'Number of News',
          backgroundColor: '#04B1CF',
          borderColor: '#04B1CF',
          data:stats.map(s=>s.totalNews)
        },

        {
          label:'Number of Actions',
          backgroundColor: '#6B7280',
          borderColor: '#6B7280',
          data:stats.map(s=>s.totalActions)
        },

      ]
    }
  })
}
}
