import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CodeExecutionService {
  private apiUrl = 'http://localhost:8080/code/execute';

  constructor(private http: HttpClient) {}

  executeCode(payload: { code: string, language: string }): Observable<any> {
    return this.http.post<any>(this.apiUrl, payload);
  }
}
