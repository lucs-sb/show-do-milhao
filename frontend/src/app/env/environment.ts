export var HTTP_OPTIONS = {
    headers: { authorization: 'Bearer ' + localStorage.getItem('authorization') }
};

export const API_URL_DEFAULT = 'http://localhost:8080/api/';