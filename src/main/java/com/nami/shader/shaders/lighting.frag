#version 330 core

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
};

struct DirectionalLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct PointLight {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

struct SpotLight {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;

    vec3 direction;
    float cutOff;
    float outerCutOff;
};

uniform vec3 viewPos;
uniform Material material;

uniform DirectionalLight directionalLights[1];
uniform PointLight pointLights[1];
uniform SpotLight spotLights[1];

in vec3 fragPos;
in vec3 normal;
in vec2 texCoords;

out vec4 FragColor;

vec3 directionalLight(DirectionalLight light, vec3 norm, vec3 viewDir) {
    vec3 lightDir = normalize(-light.direction);

    //Ambient
    vec3 ambient = light.ambient * texture(material.diffuse, texCoords).rgb;

    //Diffuse
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * texture(material.diffuse, texCoords).rgb;

    //Specular
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * spec * texture(material.specular, texCoords).rgb;

    vec3 color = ambient + diffuse + specular;
    return color;
}

vec3 pointLight(PointLight light, vec3 norm, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.position - fragPos);

    //Ambient
    vec3 ambient = light.ambient * texture(material.diffuse, texCoords).rgb;

    //Diffuse
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * texture(material.diffuse, texCoords).rgb;

    //Specular
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * spec * texture(material.specular, texCoords).rgb;

    //Attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance*distance));

    //Final Color
    vec3 color = (ambient + diffuse + specular) * attenuation;
    return color;
}

vec3 spotLight(SpotLight light, vec3 norm, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.position - fragPos);

    //Ambient
    vec3 ambient = light.ambient * texture(material.diffuse, texCoords).rgb;

    //Diffuse
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * texture(material.diffuse, texCoords).rgb;

    //Specular
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * spec * texture(material.specular, texCoords).rgb;

    //Attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * pow(distance, 2));

    //SpotLight
    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);

    //Final Color
    vec3 color = (ambient + diffuse + specular) * attenuation * intensity;
    return color;
}

void main() {
    vec3 norm = normalize(normal);
    vec3 viewDir = normalize(viewPos - fragPos);

    vec3 color = vec3(0.0);

    for (int i = 0; i < directionalLights.length(); i++)
        color += directionalLight(directionalLights[i], norm, viewDir);

    for (int i = 0; i < pointLights.length(); i++)
        color += pointLight(pointLights[i], norm, fragPos, viewDir);

    for (int i = 0; i < spotLights.length(); i++)
        color += spotLight(spotLights[i], norm, fragPos, viewDir);

    FragColor = vec4(color, 1.0);
}