package rest.helpers;

public enum AuthStates {
    NoHeader,
    InvalidToken,
    ValidToken,
    Authorized,
    NoToken,
    Unauthorized,
    ClientCred
}
