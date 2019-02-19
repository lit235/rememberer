import http from "~/services/http";
import {RecordsApi} from "~/services/api/records";

export const recordsApi: RecordsApi  = new RecordsApi(http);