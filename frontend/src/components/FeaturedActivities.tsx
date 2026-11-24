import './FeaturedActivities.css';
import { useEffect, useState } from 'react';
import { getActivities } from '../services/activityApi';
import type {Activity} from "../types/activity.ts";
import ActivityCard from "./ActivityCard.tsx";

function FeaturedActivities() {
    const [activities, setActivities] = useState<Activity[]>([]);

    useEffect(() => {
        async function loadActivities() {
            const data = await getActivities();
            setActivities(data);
        }

        loadActivities();
    }, []);

    return (
        <section className="featured-activities">
            <h2>Featured Activities</h2>
            <p>Discover something worth doing this weekend.</p>
            <div className="activity-grid">
                {activities.slice(0, 4).map((activity) => (
                    <ActivityCard key={activity.id} activity={activity} />
                ))}
            </div>
        </section>

    );
}

export default FeaturedActivities;