// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getInterfaceInvokeTop3 GET /api/analysis/top/intertface/invoke */
export async function getInterfaceInvokeTop3UsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListInterfaceInvokeVO_>('/api/analysis/top/intertface/invoke', {
    method: 'GET',
    ...(options || {}),
  });
}
