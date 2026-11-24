import type {Activity} from '../types/activity.ts';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function getActivities(): Promise<Activity[]> {
    const response = await fetch(`${API_BASE_URL}/api/activities`);

    if (!response.ok) {
        throw new Error('Failed to fetch activities');
    }

    const activities: Activity[] = await response.json();
    return activities;
}

export async function getActivityById(id: number): Promise<Activity | null> {
    const response = await fetch(`${API_BASE_URL}/api/activities/${id}`);

    if (response.status === 404) {
        return null;
    }

    if (!response.ok) {
        throw new Error('Failed to fetch activity');
    }

    const activity: Activity = await response.json();

    return activity;
}

interface ActivityFilterParams {
    categories: string[];
    price: number;
    hours: number;
    env: string | null;
}

export async function filterActivities(params: ActivityFilterParams): Promise<Activity[]> {
    const queryParams = new URLSearchParams();

    params.categories.forEach((category) => {
        queryParams.append('categories', category);
    });

    queryParams.append('price', String(params.price));
    queryParams.append('hours', String(params.hours));

    if (params.env !== null) {
        queryParams.append('env', params.env);
    }

    const response = await fetch(`${API_BASE_URL}/api/activities/filter?${queryParams.toString()}`);

    if (!response.ok) {
        throw new Error('Failed to filter activities');
    }

    const activities: Activity[] = await response.json();

    return activities;
}

