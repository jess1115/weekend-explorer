export interface Activity {
    id: number;
    name: string;
    category: string;
    price: number;
    durationHours: number;
    environment: string;
    tags: string[];
    description: string;
}