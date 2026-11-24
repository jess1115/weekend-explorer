import { Link } from 'react-router-dom';
import './ExploreByCategory.css';

const CATEGORIES = [
    { value: 'HANDMADE', label: 'Handmade' },
    { value: 'EXERCISE', label: 'Exercise' },
    { value: 'EXHIBITION', label: 'Exhibitions' },
    { value: 'LEISURE', label: 'Leisure' }
];

function ExploreByCategory() {
    return (
        <section className="explore-by-category">
            <h2>Explore by Category</h2>

            <div className="category-grid">
                {CATEGORIES.map((category) => (
                    <Link
                        to={`/explore?category=${category.value}`}
                        key={category.value}
                        className="category-card"
                    >
                        {category.label}
                    </Link>
                ))}
            </div>
        </section>
    );
}

export default ExploreByCategory;