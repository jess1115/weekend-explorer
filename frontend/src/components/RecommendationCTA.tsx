import { Link } from 'react-router-dom';
import './RecommendationCTA.css';

function RecommendationCTA() {
    return (
        <section className="recommendation-cta">
            <h2>Not sure what to do this weekend?</h2>

            <p>
                Let us help you discover activities
                that match your interests and mood.
            </p>

            <Link to="/recommend">Find Your Activity</Link>
        </section>
    );
}

export default RecommendationCTA;