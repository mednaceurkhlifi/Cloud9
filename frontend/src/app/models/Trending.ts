import { News } from "./News";
import { User } from "./User";

export class Trending {
    trendingId?: number;
    date?: string; 
    type!: string;
    score?: number;
    active?: string;
    news?: News;
    user?: User;
  }