import type { QuizAnswers } from '../types/quiz';

export interface QuizOption {
    label: string;
    value: string;
    description?: string;
}

export interface QuizQuestion {
    id: keyof QuizAnswers;
    title: string;
    description: string;
    type: 'single' | 'multiple';
    options: QuizOption[];
}

export const quizQuestions: QuizQuestion[] = [
    {
        id: 'weekendDirection',
        title: 'What does your ideal weekend look like?',
        description: 'Choose the direction that feels most like you.',
        type: 'single',
        options: [
            { label: 'Getting creative', value: 'creative', description: 'Making, crafting, and creating something new.' },
            { label: 'Seeking adventure', value: 'adventurous', description: 'Trying new challenges and exciting experiences.' },
            { label: 'Exploring culture', value: 'cultural', description: 'Discovering art, history, and local culture.' },
            { label: 'Slowing down', value: 'relaxing', description: 'Relaxing, unwinding, and recharging.' }
        ]
    },
    {
        id: 'energyLevel',
        title: 'How much energy do you have this weekend?',
        description: 'Think about the pace you would enjoy.',
        type: 'single',
        options: [
            { label: 'Taking it easy', value: 'low_energy' },
            { label: 'A balanced pace', value: 'moderate_energy' },
            { label: 'Ready for anything', value: 'high_energy' }
        ]
    },
    {
        id: 'environment',
        title: 'Where would you like to spend your time?',
        description: 'Choose the environment you prefer.',
        type: 'single',
        options: [
            { label: 'Indoors', value: 'indoors' },
            { label: 'Outdoors', value: 'outdoors' },
            { label: 'No preference', value: 'any' }
        ]
    },
    {
        id: 'interests',
        title: 'What are you interested in?',
        description: 'Select all that sound appealing.',
        type: 'multiple',
        options: [
            { label: 'Making & Crafting', value: 'making' },
            { label: 'Art & Culture', value: 'art_culture' },
            { label: 'Nature', value: 'nature' },
            { label: 'Active Experiences', value: 'active' },
            { label: 'Exploration', value: 'exploration' }
        ]
    },
    {
        id: 'learningPreference',
        title: 'Would you enjoy learning something new?',
        description: 'There is no right or wrong answer.',
        type: 'single',
        options: [
            { label: 'Absolutely', value: 'definitely' },
            { label: 'Maybe, if it sounds interesting', value: 'maybe' },
            { label: 'Not necessarily, I just want to enjoy myself', value: 'just_enjoy' }
        ]
    },
    {
        id: 'adventureLevel',
        title: 'How adventurous are you feeling?',
        description: 'Choose your comfort level.',
        type: 'single',
        options: [
            { label: 'Something familiar and comfortable', value: 'comfortable' },
            { label: 'A little something new', value: 'curious' },
            { label: 'Something bold and exciting', value: 'adventurous' }
        ]
    },
    {
        id: 'maxDuration',
        title: 'How much time would you like to spend?',
        description: 'Consider the total time you are willing to set aside.',
        type: 'single',
        options: [
            { label: 'Under 2 hours', value: 'under_2_hours' },
            { label: '2–3 hours', value: '2_to_3_hours' },
            { label: '3–5 hours', value: '3_to_5_hours' },
            { label: 'Half a day', value: 'half_day' }
        ]
    },
    {
        id: 'maxPrice',
        title: 'What is your budget for this activity?',
        description: 'Choose a comfortable spending range.',
        type: 'single',
        options: [
            { label: 'Free', value: 'free' },
            { label: 'Under NT$500', value: 'under_500' },
            { label: 'NT$500–1,000', value: '500_to_1000' },
            { label: 'NT$1,000–1,500', value: '1000_to_1500' },
            { label: 'Flexible', value: 'flexible' }
        ]
    },
    {
        id: 'companion',
        title: 'Who would you like to spend your weekend with?',
        description: 'Think about your ideal company.',
        type: 'single',
        options: [
            { label: 'Just myself', value: 'myself' },
            { label: 'My partner', value: 'partner' },
            { label: 'Friends', value: 'friends' },
            { label: 'Family', value: 'family' },
            { label: 'Anyone!', value: 'anyone' }
        ]
    },
    {
        id: 'atmosphere',
        title: 'What kind of atmosphere are you looking for?',
        description: 'Select all the feelings you would like your weekend to have.',
        type: 'multiple',
        options: [
            { label: 'Peaceful', value: 'peaceful' },
            { label: 'Inspiring', value: 'inspiring' },
            { label: 'Natural', value: 'natural' },
            { label: 'Exciting', value: 'exciting' },
            { label: 'Educational', value: 'educational' }
        ]
    }
];