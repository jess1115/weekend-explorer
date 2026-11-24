import {useEffect, useState} from 'react';
import { quizQuestions } from '../data/quizQuestions';
import type { QuizAnswers } from '../types/quiz';
import './Recommend.css';
import { getQuizRecommendations } from '../services/recommendationApi';
import type { ActivityRecommendation } from '../types/recommendation';
import ActivityCard from "../components/ActivityCard.tsx";

const INITIAL_ANSWERS: QuizAnswers = {
    weekendDirection: '',
    energyLevel: '',
    environment: '',
    interests: [],
    learningPreference: '',
    adventureLevel: '',
    maxDuration: '',
    maxPrice: '',
    companion: '',
    atmosphere: []
};

interface QuizSession {
    currentQuestionIndex: number;
    answers: QuizAnswers;
    recommendations: ActivityRecommendation[];
    showResults: boolean;
}

function Recommend() {
    const getSavedSession = (): QuizSession | null => {
        try {
            const savedSession = sessionStorage.getItem('recommendQuiz');

            return savedSession ? JSON.parse(savedSession) : null;

        } catch (error) {
            console.error('Failed to restore quiz session:', error);
            sessionStorage.removeItem('recommendQuiz');
            return null;
        }
    };

    const [initialSession] = useState<QuizSession | null>(() => getSavedSession());

    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(initialSession?.currentQuestionIndex ?? 0);
    const [answers, setAnswers] = useState<QuizAnswers>(initialSession?.answers ?? INITIAL_ANSWERS);
    const currentQuestion = quizQuestions[currentQuestionIndex];
    const totalQuestions = quizQuestions.length;
    const progress = ((currentQuestionIndex + 1) / totalQuestions) * 100;
    const [recommendations, setRecommendations] =
        useState<ActivityRecommendation[]>(initialSession?.recommendations ?? []);

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitError, setSubmitError] = useState('');
    const [showResults, setShowResults] = useState( initialSession?.showResults ?? false);

    useEffect(() => {
        const session: QuizSession = {
            currentQuestionIndex,
            answers,
            recommendations,
            showResults
        };

        sessionStorage.setItem(
            'recommendQuiz',
            JSON.stringify(session)
        );

    }, [
        currentQuestionIndex,
        answers,
        recommendations,
        showResults
    ]);



    const isCurrentQuestionAnswered = () => {
        const answer = answers[currentQuestion.id];

        if (Array.isArray(answer)) {
            return answer.length > 0;
        }

        return answer !== '';
    };

    const handlePrevious = () => {
        setCurrentQuestionIndex((previousIndex) => previousIndex - 1);
    };

    const handleNext = () => {
        if (!isCurrentQuestionAnswered()) {
            return;
        }

        if (currentQuestionIndex < totalQuestions - 1) {
            setCurrentQuestionIndex((previousIndex) => previousIndex + 1);
        }
    };

    const handleSubmit = async () => {
        if (!isCurrentQuestionAnswered()) {
            return;
        }

        try {
            setIsSubmitting(true);
            setSubmitError('');

            const results = await getQuizRecommendations(answers);

            setRecommendations(results);
            setShowResults(true);


        } catch (error) {
            console.error('Failed to get recommendations:', error);
            setSubmitError('Something went wrong. Please try again.');

        } finally {
            setIsSubmitting(false);
        }
    };

    const isSelected = (value: string) => {
        const answer = answers[currentQuestion.id];

        if (Array.isArray(answer)) {
            return answer.includes(value);
        }

        return answer === value;
    };

    const handleOptionSelect = (value: string) => {
        setAnswers((previousAnswers) => {
            const currentAnswer = previousAnswers[currentQuestion.id];

            // Single choice
            if (currentQuestion.type === 'single') {
                return {
                    ...previousAnswers,
                    [currentQuestion.id]: value
                };
            }

            // Multiple choice
            const selectedValues = currentAnswer as string[];

            const updatedValues = selectedValues.includes(value)
                ? selectedValues.filter((item) => item !== value)
                : [...selectedValues, value];

            return {
                ...previousAnswers,
                [currentQuestion.id]: updatedValues
            };
        });
    };

    const handleRetakeQuiz = () => {
        setAnswers(INITIAL_ANSWERS);
        setRecommendations([]);
        setSubmitError('');
        setCurrentQuestionIndex(0);
        setShowResults(false);

        sessionStorage.removeItem('recommendQuiz');

        window.scrollTo({
            top: 300,
            behavior: 'smooth'
        });
    };

    return (
        <main className="recommend-page">
            <section className="recommend-hero">
            <span className="recommend-eyebrow">
                YOUR WEEKEND, YOUR WAY
            </span>
                <h1>Find your kind of weekend.</h1>
                <p>
                    Answer a few questions about your preferences,
                    and discover activities that match your mood.
                </p>
            </section>

            {!showResults ? (
                <section className="recommend-quiz">
                    <div className="quiz-progress">
                        <div className="quiz-progress-info">
                    <span>
                        Question {currentQuestionIndex + 1} of {totalQuestions}
                    </span>
                            <span>{Math.round(progress)}%</span>
                        </div>

                        <div className="quiz-progress-track">
                            <div
                                className="quiz-progress-fill"
                                style={{ width: `${progress}%` }}
                            />
                        </div>
                    </div>

                    <div className="quiz-question">
                        <h2>{currentQuestion.title}</h2>
                        <p>{currentQuestion.description}</p>
                    </div>

                    <div className="quiz-options">
                        {currentQuestion.options.map((option) => (
                            <button
                                key={option.value}
                                type="button"
                                className={`quiz-option ${isSelected(option.value) ? 'selected' : ''}`}
                                onClick={() => handleOptionSelect(option.value)}
                            >
                            <span className="quiz-option-label">
                                {option.label}
                            </span>

                                {option.description && (
                                    <span className="quiz-option-description">
                                    {option.description}
                                </span>
                                )}
                            </button>
                        ))}
                    </div>

                    <div className="quiz-navigation">
                        {currentQuestionIndex > 0 && (
                            <button
                                type="button"
                                className="quiz-previous"
                                onClick={handlePrevious}
                            >
                                Previous
                            </button>
                        )}

                        {currentQuestionIndex < totalQuestions - 1 ? (
                            <button
                                type="button"
                                className="quiz-next"
                                onClick={handleNext}
                                disabled={!isCurrentQuestionAnswered()}
                            >
                                Next
                            </button>
                        ) : (
                            <button
                                type="button"
                                className="quiz-next"
                                onClick={handleSubmit}
                                disabled={!isCurrentQuestionAnswered() || isSubmitting}
                            >
                                {isSubmitting ? 'Finding Activities...' : 'Discover Activities'}
                            </button>
                        )}
                    </div>

                    {submitError && (
                        <p className="quiz-error">
                            {submitError}
                        </p>
                    )}

                </section>
            ) : (
                <section className="recommend-results">
                    <h2>Your Weekend Matches</h2>
                    <p>Here are the activities we found for you.</p>

                    {recommendations.length > 0 ? (
                        <div className="recommend-results-grid">
                            {recommendations.map((recommendation) => (
                                <div key={recommendation.activity.id}>
                                    <p className="recommendation-score">
                                        MATCH SCORE · {recommendation.score}
                                    </p>

                                    <ActivityCard activity={recommendation.activity} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="recommend-empty">
                            <h3>No matches found just yet.</h3>
                            <p>
                                Try adjusting your preferences to discover more activities.
                            </p>
                        </div>
                    )}

                    <button
                        type="button"
                        className="retake-quiz-button"
                        onClick={handleRetakeQuiz}
                    >
                        Retake Quiz
                    </button>
                </section>



            )}


        </main>
    );
}

export default Recommend;