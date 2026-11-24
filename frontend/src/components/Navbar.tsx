import './Navbar.css';
import {NavLink} from 'react-router-dom';


function Navbar() {
    return (
        <nav className="navbar">

            <h1 className="brand-name">Weekend Explorer</h1>
            <div className="navbar-links">
                <NavLink to="/">Home</NavLink>
                <NavLink to="/explore">Explore</NavLink>
                <NavLink to="/recommend">Recommend</NavLink>
                <NavLink to="/saved">Saved</NavLink>
            </div>
        </nav>
    );
}

export default Navbar;