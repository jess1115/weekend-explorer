const SAVED_ACTIVITIES_KEY = 'savedActivities';

export function getSavedActivityIds(): number[] {
    const saved = localStorage.getItem(SAVED_ACTIVITIES_KEY);

    if (!saved) {
        return [];
    }

    return JSON.parse(saved);
}

export function addSavedActivity(id: number): void {
    const savedIds = getSavedActivityIds();

    if (savedIds.includes(id)) {
        return;
    }

    const updatedIds = [...savedIds, id];

    localStorage.setItem(SAVED_ACTIVITIES_KEY, JSON.stringify(updatedIds));
}

export function removeSavedActivity(id: number): void {
    const savedIds = getSavedActivityIds();

    const updatedIds = savedIds.filter(
        (savedId) => savedId !== id
    );

    localStorage.setItem(SAVED_ACTIVITIES_KEY, JSON.stringify(updatedIds));
}