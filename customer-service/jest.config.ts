module.exports = {
    // Other Jest configurations...
    preset: "ts-jest",
    testEnvironment: "node",
    testPathIgnorePatterns: ['/node_modules/', '<rootDir>/__tests__/[^.]*$','<rootDir>/__tests__/testHelpers.ts'],
};
