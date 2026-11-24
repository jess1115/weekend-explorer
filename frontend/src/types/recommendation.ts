import type { Activity } from './activity';

export interface ActivityRecommendation {
    activity: Activity;
    score: number;
}