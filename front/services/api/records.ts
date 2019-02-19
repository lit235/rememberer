import {AxiosInstance} from "~/node_modules/axios";
import {ApiResponse, ResponseStatus, Record} from "~/types/api";

export class RecordsApi {
    private axiosInstance: AxiosInstance;

    constructor(axiosInstance: AxiosInstance) {
        this.axiosInstance = axiosInstance;
    }

    public async storeRecord(record: Record): Promise<ApiResponse> {
        try {
            const response = await this.axiosInstance.post("/records/", record);

            return new Promise<ApiResponse>(resolve => {
                resolve({
                    response: response.data,
                    status: ResponseStatus.SUCCESS
                });
            });

        } catch (e) {
            return new Promise<ApiResponse>(resolve => {
                resolve({
                    response: e.response ? e.response.data : e.message,
                    status: ResponseStatus.FAIL
                });
            });
        }
    }

    public async restoreRecord(code: string): Promise<ApiResponse> {
        try {
            const response = await this.axiosInstance.get(`/records/${code}`);

            return new Promise<ApiResponse>(resolve => {
                resolve({
                    response: response.data,
                    status: ResponseStatus.SUCCESS
                });
            });

        } catch (e) {
            return new Promise<ApiResponse>(resolve => {
                resolve({
                    response: e.response ? e.response.data : e.message,
                    status: ResponseStatus.FAIL
                });
            });
        }
    }
}