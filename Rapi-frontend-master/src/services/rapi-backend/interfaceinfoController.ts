// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addInterfaceinfo POST /api/interfaceinfo/add */
export async function addInterfaceinfoUsingPost(
  body: API.InterfaceinfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/interfaceinfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteInterfaceinfo POST /api/interfaceinfo/delete */
export async function deleteInterfaceinfoUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfaceinfo/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getInterfaceinfoVOById GET /api/interfaceinfo/get/vo */
export async function getInterfaceinfoVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfaceinfoVOByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceinfoVO_>('/api/interfaceinfo/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** invokeInterfaceinfo POST /api/interfaceinfo/invoke */
export async function invokeInterfaceinfoUsingPost(
  body: API.InterfaceinfoInvokeRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseObject_>('/api/interfaceinfo/invoke', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listInterfaceinfoByPage POST /api/interfaceinfo/list/page */
export async function listInterfaceinfoByPageUsingPost(
  body: API.InterfaceinfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfaceInfo_>('/api/interfaceinfo/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listInterfaceinfoVOByPage POST /api/interfaceinfo/list/page/vo */
export async function listInterfaceinfoVoByPageUsingPost(
  body: API.InterfaceinfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfaceinfoVO_>('/api/interfaceinfo/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyInterfaceinfoVOByPage POST /api/interfaceinfo/my/list/page/vo */
export async function listMyInterfaceinfoVoByPageUsingPost(
  body: API.InterfaceinfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfaceinfoVO_>('/api/interfaceinfo/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** offlineInterfaceinfo POST /api/interfaceinfo/offline */
export async function offlineInterfaceinfoUsingPost(
  body: API.IdRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfaceinfo/offline', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** onlineInterfaceinfo POST /api/interfaceinfo/online */
export async function onlineInterfaceinfoUsingPost(
  body: API.IdRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfaceinfo/online', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateInterfaceinfo POST /api/interfaceinfo/update */
export async function updateInterfaceinfoUsingPost(
  body: API.InterfaceinfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/interfaceinfo/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
