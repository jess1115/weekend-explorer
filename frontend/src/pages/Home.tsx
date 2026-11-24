import HeroSection from "../components/HeroSection.tsx";
import FeaturedActivities from "../components/FeaturedActivities.tsx";
import ExploreByCategory from "../components/ExploreByCategory.tsx";
import RecommendationCTA from "../components/RecommendationCTA.tsx";

function Home() {
    return (
        <main>
            <HeroSection />
            <FeaturedActivities />
            <ExploreByCategory />
            <RecommendationCTA />
        </main>
    );
}

export default Home;