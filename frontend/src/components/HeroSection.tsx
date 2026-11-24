import './HeroSection.css';
import { Link } from 'react-router-dom';

function HeroSection() {
    return (
        <section className="hero">
            <p className="hero-eyebrow">WEEKEND EXPLORER</p>

            <h1>Make your weekends meaningful.</h1>

            <p>Discover activities that inspire you to explore, create, and enjoy your free time.</p>

            <Link to="/explore" className="hero-button">Explore Activities</Link>
        </section>
    );
}

export default HeroSection;