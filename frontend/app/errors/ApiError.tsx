export class ApiError extends Error {
  public statusCode: number;
  public details?: unknown;

  constructor(message: string, statusCode: number = 500, details?: unknown) {
    super(message);
    this.name = "ApiError";
    this.statusCode = statusCode;
    this.details = details;

    // Maintains proper stack trace (only in V8 engines)
    if ((Error as ErrorConstructor & { captureStackTrace?: (targetObject: object, constructorOpt?: new (...args: unknown[]) => object) => void }).captureStackTrace) {
      (Error as ErrorConstructor & { captureStackTrace?: (targetObject: object, constructorOpt?: new (...args: unknown[]) => object) => void }).captureStackTrace!(this, ApiError);
    }
  }
}