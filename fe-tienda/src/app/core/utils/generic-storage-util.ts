export abstract class GenericStorage implements Storage {

    constructor(protected readonly api: Storage) {
    }

    [name: string]: any;
    length: number;
    
    setItem(key: string, value: unknown): void {
        let data = JSON.stringify(value);
        this.api.setItem(key, data);
    }

    getItem<T> (key: string): T | null {
        let data = this.api.getItem(key);
        if(data !== null) {
            return JSON.parse(data) as T;
        }
        return null;
    }

    clear(): void {
        this.api.clear();
    }
    
    key(index: number): string | null {
        return this.api.key(index);
    }

    removeItem(key: string): void {
       this.api.removeItem(key);
    }
    
}