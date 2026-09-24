export const msalConfig = {
  auth: {
    clientId: "70dd5813-12fe-4726-a54a-ac35ae2642de",
    authority: "https://login.microsoftonline.com/acbbd823-5223-4671-93af-34842f9b5074",
    redirectUri: "http://localhost:5173",
  },
  cache: {
    cacheLocation: "sessionStorage",
    storeAuthStateInCookie: false,
  },
};

export const loginRequest = {
  scopes: [
    "api://ceb17b53-3089-43a0-9525-d796aec2d811/OT.Create"
  ],
  prompt: "select_account",
};
