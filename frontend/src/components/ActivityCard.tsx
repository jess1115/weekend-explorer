import type { Activity } from '../types/activity';
import { Link } from 'react-router-dom';
import './ActivityCard.css';

interface ActivityCardProps {
    activity: Activity;
    onRemoveSaved?: (id: number) => void;
}

function ActivityCard({ activity, onRemoveSaved }: ActivityCardProps) {
    return (
        <article className="activity-card">
            <h3>{activity.name}</h3>
            <p>{activity.description}</p>

            <div className="activity-meta">
                <span>NT$ {activity.price}</span>
                <span>{activity.durationHours} hours</span>
            </div>

            <Link to={`/activities/${activity.id}`} className="activity-card-link">
                View Details →
            </Link>

            {onRemoveSaved && (
                <button type="button" className="remove-saved-button" onClick={() => onRemoveSaved(activity.id)}>
                    Remove from Saved
                </button>
            )}
        </article>
    );
}

export default ActivityCard;