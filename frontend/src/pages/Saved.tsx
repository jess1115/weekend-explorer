import { useEffect, useState } from 'react';
import type { Activity } from '../types/activity';
import { getActivities } from '../services/activityApi';
import { getSavedActivityIds, removeSavedActivity } from '../services/savedActivity';
import ActivityCard from '../components/ActivityCard';
import './Saved.css';
import {Link} from "react-router-dom";
import LoadingState from "../components/LoadingState.tsx";

function Saved() {
    const [activities, setActivities] = useState<Activity[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function loadSavedActivities() {
            try {
                const savedIds = getSavedActivityIds();

                if (savedIds.length === 0) {
                    setActivities([]);
                    return;
                }

                const allActivities = await getActivities();

                const savedActivities = allActivities.filter(
                    (activity) => savedIds.includes(activity.id)
                );

                setActivities(savedActivities);

            } finally {
                setLoading(false);
            }
        }

        loadSavedActivities();
    }, []);

    function handleRemoveSaved(id: number) {
        removeSavedActivity(id);

        setActivities((prevActivities) =>
            prevActivities.filter(
                (activity) => activity.id !== id
            )
        );
    }

    return  (
        <main className="saved-page">
            <header className="saved-header">
                <h1>Saved Activities</h1>
                <p>Your collection of activities worth looking forward to.</p>
            </header>

            {loading ? (
                <LoadingState message="Loading your saved activities..." />
            ) : activities.length === 0 ? (
                <div className="saved-empty">
                    <h2>No saved activities yet</h2>
                    <p>
                        Explore activities and save the ones you'd love to try.
                    </p>
                    <Link to="/explore" className="saved-explore-link">
                        Explore Activities →
                    </Link>
                </div>
            ) : (
                <div className="activity-grid">
                    {activities.map((activity) => (
                        <ActivityCard
                            key={activity.id}
                            activity={activity}
                            onRemoveSaved={handleRemoveSaved}
                        />
                    ))}
                </div>
            )}
        </main>
    );
}

export default Saved;