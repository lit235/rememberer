export enum ResponseStatus {
    FAIL, SUCCESS
};

export interface ApiResponse {
    response: Object,
    status: ResponseStatus
}

export interface Record {
    text: string;
}