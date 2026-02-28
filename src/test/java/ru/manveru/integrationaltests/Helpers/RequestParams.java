package ru.manveru.integrationaltests.Helpers;

import java.util.Map;

public class RequestParams {
    private String endpoint;
    private Object body;
    private Map<String, String> queryParams;
    private RequestType type;
    
    // Приватный конструктор для использования Builder
    private RequestParams(Builder builder) {
        this.endpoint = builder.endpoint;
        this.body = builder.body;
        this.queryParams = builder.queryParams;
        this.type = builder.type;
    }
    
    // Геттеры
    public String getEndpoint() { return endpoint; }
    public Object getBody() { return body; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public RequestType getType() { return type; }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Query params:");
        
        if (this.endpoint != null)
            sb.append(" endpoint = ").append(this.endpoint);
        if (this.body != null)
            sb.append(" body = ").append(this.body);
        if (this.queryParams != null)
            sb.append(" queryParams = ").append(this.queryParams);
        if (this.type != null)
            sb.append(" type = ").append(this.type);
        return sb.toString();
    }
    
    // Builder класс
    public static class Builder {
        private String endpoint;
        private Object body;
        private Map<String, String> queryParams;
        private RequestType type;
        
        public Builder(String endpoint, RequestType type) {
            this.endpoint = endpoint;
            this.type = type;
        }
        
        public Builder withBody(Object body) {
            this.body = body;
            return this;
        }
        
        public Builder withQueryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }
        
        public RequestParams build() {
            return new RequestParams(this);
        }
    }
    
    // Вспомогательные статические методы для быстрого создания
    public static Builder get(String endpoint) {
        return new Builder(endpoint, RequestType.GET);
    }
    
    public static Builder post(String endpoint) {
        return new Builder(endpoint, RequestType.POST);
    }
    
    public static Builder put(String endpoint) {
        return new Builder(endpoint, RequestType.PUT);
    }
    
    public static Builder patch(String endpoint) {
        return new Builder(endpoint, RequestType.PATCH);
    }
    
    public static Builder delete(String endpoint) {
        return new Builder(endpoint, RequestType.DELETE);
    }
}
