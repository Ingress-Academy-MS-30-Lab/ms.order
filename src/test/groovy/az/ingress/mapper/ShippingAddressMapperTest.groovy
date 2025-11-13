package az.ingress.mapper


import az.ingress.model.dto.request.ShippingAddressRequest
import az.ingress.model.mapper.ShippingAddressMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class ShippingAddressMapperTest extends Specification {

    ShippingAddressMapper mapper = Mappers.getMapper(ShippingAddressMapper)
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "should map ShippingAddressRequest to ShippingAddressEntity successfully"() {
        given:
        def addressRequest = random.nextObject(ShippingAddressRequest)
        when:
        def addressEntity = mapper.toEntity(addressRequest)
        then:
        addressEntity != null
        addressEntity.fullName == addressRequest.fullName
        addressEntity.region == addressRequest.region
    }

    def "should return null when request is null"() {
        when:
        def entity = mapper.toEntity(null)
        then:
        entity == null
    }

    def "should handle empty fields gracefully"() {
        given:
        def request = new ShippingAddressRequest()

        when:
        def entity = mapper.toEntity(request)

        then:
        entity != null
        entity.city == null
        entity.fullName == null
        entity.postalCode == null
    }
}
