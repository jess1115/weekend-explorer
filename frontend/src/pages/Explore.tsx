import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import type { Activity } from '../types/activity';
import { filterActivities } from '../services/activityApi';
import ActivityCard from '../components/ActivityCard';
import './Explore.css';
import LoadingState from '../components/LoadingState';
import ErrorState from '../components/ErrorState';


const CATEGORY_OPTIONS = [
    { value: 'HANDMADE', label: 'Handmade' },
    { value: 'EXERCISE', label: 'Exercise' },
    { value: 'EXHIBITION', label: 'Exhibitions' },
    { value: 'LEISURE', label: 'Leisure' }
];

const ENVIRONMENT_OPTIONS = [
    { value: 'INDOOR', label: 'Indoor' },
    { value: 'OUTDOOR', label: 'Outdoor' }
];

function Explore() {
    const [searchParams, setSearchParams] = useSearchParams();
    const [activities, setActivities] = useState<Activity[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [maxPrice, setMaxPrice] = useState(Number(searchParams.get('price') ?? 2000));
    const [maxDuration, setMaxDuration] = useState(Number(searchParams.get('hours')) || 6);
    const [selectedCategories, setSelectedCategories] = useState<string[]>(searchParams.getAll('category'));
    const [selectedEnvironments, setSelectedEnvironments] =
        useState<string[]>(searchParams.get('env') ? [searchParams.get('env')!] : []);

    useEffect(() => {
        const params = new URLSearchParams();

        params.set('price', String(maxPrice));
        params.set('hours', String(maxDuration));

        selectedCategories.forEach((category) => {
            params.append('category', category);
        });

        if (selectedEnvironments.length === 1) {
            params.set('env', selectedEnvironments[0]);
        }

        setSearchParams(params, { replace: true });

    }, [
        maxPrice,
        maxDuration,
        selectedCategories,
        selectedEnvironments,
        setSearchParams
    ]);

    useEffect(() => {
        let isActive = true;

        const timer = setTimeout(() => {
            async function loadActivities() {
                try {
                    setError(null);

                    const env =
                        selectedEnvironments.length === 1
                            ? selectedEnvironments[0]
                            : null;

                    const data = await filterActivities({
                        categories: selectedCategories,
                        price: maxPrice,
                        hours: maxDuration,
                        env: env
                    });

                    if (isActive) {
                        setActivities(data);
                    }
                } catch (err) {
                    if (isActive) {
                        setError('Failed to load activities');
                    }
                } finally {
                    if (isActive) {
                        setLoading(false);
                    }
                }
            }

            loadActivities();
        }, 500);

        return () => {
            isActive = false;
            clearTimeout(timer);
        }

    }, [
        selectedCategories,
        selectedEnvironments,
        maxPrice,
        maxDuration
    ]);

    if (loading) {
        return <LoadingState message="Finding activities for you..." />;
    }

    if (error) {
        return <ErrorState message={error} />;
    }

    return (
        <main className="explore-page">
            <header className="explore-header">
                <h1>Explore Activities</h1>
                <p>
                    Discover something new to do this weekend.
                </p>
            </header>


            <div className="explore-layout">

                <aside className="filter-panel">
                    <h2>Filters</h2>

                    <div className="filter-group">
                        <div className="filter-heading">
                            <h3>Maximum Price</h3>
                            <span>NT$ {maxPrice}</span>
                        </div>

                        <input
                            className="filter-range"
                            type="range"
                            min="0"
                            max="3000"
                            step="100"
                            value={maxPrice}
                            onChange={(event) =>
                                setMaxPrice(Number(event.target.value))}
                        />

                        <div className="range-labels">
                            <span>NT$0</span>
                            <span>NT$3,000</span>
                        </div>
                    </div>

                    <div className="filter-group">
                        <div className="filter-heading">
                            <h3>Maximum Duration</h3>
                            <span>{maxDuration} {maxDuration === 1 ? 'hour' : 'hours'}</span>
                        </div>

                        <input
                            className="filter-range"
                            type="range"
                            min="1"
                            max="12"
                            step="1"
                            value={maxDuration}
                            onChange={(event) =>
                                setMaxDuration(Number(event.target.value))}
                        />

                        <div className="range-labels">
                            <span>1 hour</span>
                            <span>12 hours</span>
                        </div>
                    </div>

                    <div className="filter-group">
                        <h3>Category</h3>
                        {CATEGORY_OPTIONS.map((category) => (
                            <label key={category.value}>
                                <input
                                    type="checkbox"
                                    value={category.value}
                                    checked={selectedCategories.includes(category.value)}
                                    onChange={(event) => {
                                        if (event.target.checked) {
                                            setSelectedCategories([...selectedCategories, category.value]);
                                        } else {
                                            setSelectedCategories(
                                                selectedCategories.filter((item) =>
                                                    item !== category.value)
                                            );
                                        }
                                    }}
                                />
                                {category.label}
                            </label>
                        ))}
                    </div>

                    <div className="filter-group">
                        <h3>Environment</h3>
                        {ENVIRONMENT_OPTIONS.map((environment) => (
                            <label key={environment.value}>
                                <input
                                    type="checkbox"
                                    value={environment.value}
                                    checked={selectedEnvironments.includes(environment.value)}
                                    onChange={(event) => {
                                        if (event.target.checked) {
                                            setSelectedEnvironments([...selectedEnvironments, environment.value]);
                                        } else {
                                            setSelectedEnvironments(
                                                selectedEnvironments.filter((item) =>
                                                    item !== environment.value)
                                            );
                                        }
                                    }}
                                />
                                {environment.label}
                            </label>
                        ))}
                    </div>
                </aside>

                <section className="explore-results">
                    <p className="activity-count">
                        {activities.length} activities found
                    </p>

                    {activities.length === 0 ? (
                        <p className="no-activities">No activities found.</p>
                    ) : (
                        <div className="activity-grid">
                            {activities.map((activity) => (
                                <ActivityCard
                                    key={activity.id}
                                    activity={activity}
                                />
                            ))}
                        </div>
                    )}
                </section>

            </div>

        </main>
    );
}

export default Explore;