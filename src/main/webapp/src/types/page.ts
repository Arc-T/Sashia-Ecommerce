export interface PaginatedRequest {
    page: number;
    size: number;
}

export interface PaginatedResponse<T> {
    content: T[];
    page: {
        size: number;
        number: number;
        totalElements: number;
        totalPages: number;
    };
}

export const UNPAGED = {
    page: 0,
    size: 5
}