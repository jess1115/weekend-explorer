import './LoadingState.css';

interface LoadingStateProps {
    message?: string;
}

function LoadingState({message = 'Loading...'}: LoadingStateProps) {
    return (
        <div className="loading-state">
            <div className="loading-spinner"></div>
            <p>{message}</p>
        </div>
    );
}

export default LoadingState;