import {Link, useNavigate, useParams} from 'react-router-dom';
import { useEffect, useState } from 'react';
import type { Activity } from '../types/activity';
import { getActivityById } from '../services/activityApi';
import './ActivityDetail.css';
import {getSavedActivityIds, addSavedActivity, removeSavedActivity} from '../services/savedActivity';
import LoadingState from "../components/LoadingState.tsx";
import ErrorState from "../components/ErrorState.tsx";

function ActivityDetail() {
    const { id } = useParams();

    const [activity, setActivity] = useState<Activity | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isSaved, setIsSaved] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        async function loadActivity() {
            try {
                if (!id) {
                    throw new Error('Invalid activity ID');
                }

                setLoading(true);
                setError(null);

                const data = await getActivityById(Number(id));

                if (!data) {
                    setActivity(null);
                    return;
                }

                setActivity(data);
                setIsSaved(getSavedActivityIds().includes(data.id));

            } catch (err) {
                setError('Failed to load activity');
            } finally {
                setLoading(false);
            }
        }

        loadActivity();
    }, [id]);

    if (loading) {
        return <LoadingState message="Loading activity details..." />;
    }

    if (error) {
        return <ErrorState message={error} />;
    }

    if (!activity) {
        return (
            <div className="activity-not-found">
                <span className="not-found-label">404 — NOT FOUND</span>

                <h2>Oh, this adventure is missing.</h2>

                <p>
                    We couldn't find the activity you're looking for.
                    It may have been removed or is no longer available.
                </p>

                <Link to="/explore" className="not-found-link">
                    Explore other activities
                </Link>
            </div>
        );
    }

    function handleSaveToggle() {
        if (!activity) {
            return;
        }

        if (isSaved) {
            removeSavedActivity(activity.id);
        } else {
            addSavedActivity(activity.id);
        }

        setIsSaved(!isSaved);
    }

    return (
        <main className="activity-detail">

            <button
                className="back-link"
                onClick={() => navigate(-1)}
            >
                ← Back
            </button>

            <div className="activity-detail-content">
                <img src="/images/activity-placeholder.jpg" alt={activity.name}/>

                <div className="activity-detail-header">

                    <h1>{activity.name}</h1>

                    <p>{activity.description}</p>
                </div>

                <div className="activity-detail-info">
                    <div>
                        <span>CATEGORY</span>
                        <p>{activity.category}</p>
                    </div>

                    <div>
                        <span>ENVIRONMENT</span>
                        <p>{activity.environment}</p>
                    </div>

                    <div>
                        <span>PRICE</span>
                        <p>NT$ {activity.price}</p>
                    </div>

                    <div>
                        <span>DURATION</span>
                        <p>{activity.durationHours} hours</p>
                    </div>
                </div>


                <div className="activity-detail-tags">
                    <h2>Interests</h2>

                    <div>
                        {activity.tags.map((tag) => (
                            <span key={tag}>{tag}</span>
                        ))}
                    </div>
                </div>

                <button type="button" className="save-button" onClick={handleSaveToggle}>
                    {isSaved ? '❤️ Saved' : '🩶 Save'}
                </button>

            </div>
        </main>
    );
}

export default ActivityDetail;