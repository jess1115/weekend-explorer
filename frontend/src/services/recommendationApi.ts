import type { QuizAnswers } from '../types/quiz';
import type { ActivityRecommendation } from '../types/recommendation';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function getQuizRecommendations(answers: QuizAnswers): Promise<ActivityRecommendation[]> {

    const response = await fetch(
        `${API_BASE_URL}/api/activities/recommend/quiz`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(answers)
        }
    );

    if (!response.ok) {
        throw new Error('Failed to get recommendations');
    }

    const recommendations: ActivityRecommendation[] =
        await response.json();

    return recommendations;
}